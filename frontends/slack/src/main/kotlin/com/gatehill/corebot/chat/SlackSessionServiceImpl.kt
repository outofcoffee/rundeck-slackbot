package com.gatehill.corebot.chat

import com.gatehill.corebot.config.ConfigService
import com.ullink.slack.simpleslackapi.SlackChannel
import com.gatehill.corebot.config.ChatSettings
import com.ullink.slack.simpleslackapi.SlackPreparedMessage
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.SlackConnectedListener
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import javax.inject.Inject

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
open class SlackSessionServiceImpl @Inject constructor(configService: ConfigService) : SlackSessionService {
    private val logger: Logger = LogManager.getLogger(SlackSessionServiceImpl::class.java)

    override val session: SlackSession by lazy {
        logger.info("Connecting to Slack...")

        val session = SlackSessionFactory.createWebSocketSlackSession(ChatSettings.chat.authToken)
        connectedListeners.forEach { session.addSlackConnectedListener(it) }
        session.connect()

        logger.info("Connected to Slack [persona=${session.sessionPersona().userName}]")
        session
    }

    /**
     * Allow subclasses to hook into Slack events.
     */
    protected open val connectedListeners = listOf(SlackConnectedListener { _, theSession ->
        ChatSettings.chat.channelNames.forEach {
            val joinMessage = configService.joinMessage ?:
                    "${ChatLines.greeting()} :simple_smile: ${ChatLines.ready()}."

            theSession.findChannelByName(it)?.let { channel -> theSession.sendMessage(channel, joinMessage) }
                    ?: logger.warn("Unable to find channel: $it")
        }
    })

    override val botUsername: String
        get() = session.sessionPersona().userName

    override fun sendMessage(channelId: String, triggerMessageTimestamp: String, message: String) {
        sendMessage(session.findChannelById(channelId), triggerMessageTimestamp, message)
    }

    override fun sendMessage(channel: SlackChannel, triggerMessageTimestamp: String, message: String) {
        val messageBuilder = SlackPreparedMessage.Builder().apply {
            withMessage(message)
            withUnfurl(true)

            if (ChatSettings.chat.replyInThread) {
                // according to: https://api.slack.com/docs/message-threading
                // using the message timestamp is enough to attach a reply to a thread
                withThreadTimestamp(triggerMessageTimestamp)
            }
        }

        session.sendMessage(channel, messageBuilder.build())
    }

    override fun addReaction(channelId: String, triggerMessageTimestamp: String, emojiCode: String) {
        session.addReactionToMessage(session.findChannelById(channelId), triggerMessageTimestamp, emojiCode)
    }
}
