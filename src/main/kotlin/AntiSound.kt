import com.lambda.client.event.events.PacketEvent
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.client.util.threads.safeListener
import net.minecraft.init.SoundEvents
import net.minecraft.network.play.server.SPacketEffect
import net.minecraft.network.play.server.SPacketSoundEffect

internal object AntiSound: PluginModule(
    name = "AntiSound",
    category = Category.MISC,
    description = "Prevents some loud/Global sounds from playing",
    pluginMain = AntiSoundLoader
) {
    private val checkSound by setting("Check Sound", Cancel.ALL)
    private val wither by setting("Wither", true)
    private val enderDragon by setting("Ender Dragon", true)
    private val endPortal by setting("End Portal", true)
    private val lightning by setting("Lightning", true, { checkSound == Cancel.ALL })
    private val debug by setting("Notify", false)
    init {
        safeListener<PacketEvent.Receive> {
            if (it.packet is SPacketEffect) {
                val effect = it.packet as SPacketEffect

                when (effect.soundType) {
                    1023 -> {
                        if (wither) {
                            it.cancel()
                            if (debug) MessageSendHelper.sendWarningMessage("Cancelled wither spawn")
                        }
                    }

                    1024 -> {
                        if (wither) {
                            it.cancel()
                            if (debug) MessageSendHelper.sendWarningMessage("Cancelled wither fire")
                        }
                    }

                    1028 -> {
                        if (enderDragon) {
                            it.cancel()
                            if (debug) MessageSendHelper.sendWarningMessage("Cancelled ender dragon")
                        }
                    }
                    1038 -> {
                        if (endPortal) {
                            it.cancel()
                            if (debug) MessageSendHelper.sendWarningMessage("Cancelled dragon death")
                        }
                    }
                }
            }

            if (it.packet is SPacketSoundEffect && checkSound == Cancel.ALL) {
                val sound = it.packet as SPacketSoundEffect

                if (wither && sound.sound == SoundEvents.ENTITY_WITHER_DEATH
                    || sound.sound == SoundEvents.ENTITY_WITHER_HURT
                    || sound.sound == SoundEvents.ENTITY_WITHER_SHOOT
                    || sound.sound == SoundEvents.ENTITY_WITHER_SPAWN
                    || sound.sound == SoundEvents.ENTITY_WITHER_AMBIENT
                    || sound.sound == SoundEvents.ENTITY_WITHER_BREAK_BLOCK) {
                    it.cancel()
                    if (debug) MessageSendHelper.sendWarningMessage("Cancelled wither sound")
                }

                if (enderDragon && sound.sound == SoundEvents.ENTITY_ENDERDRAGON_AMBIENT
                    || sound.sound == SoundEvents.ENTITY_ENDERDRAGON_DEATH
                    || sound.sound == SoundEvents.ENTITY_ENDERDRAGON_HURT
                    || sound.sound == SoundEvents.ENTITY_ENDERDRAGON_FLAP
                    || sound.sound == SoundEvents.ENTITY_ENDERDRAGON_GROWL
                    || sound.sound == SoundEvents.ENTITY_ENDERDRAGON_SHOOT
                    || sound.sound == SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD) {
                    it.cancel()
                    if (debug) MessageSendHelper.sendWarningMessage("Cancelled enderdragon sound")
                }

                if (lightning && checkSound == Cancel.ALL && sound.sound == SoundEvents.ENTITY_LIGHTNING_THUNDER
                    || sound.sound == SoundEvents.ENTITY_LIGHTNING_IMPACT) {
                    it.cancel()
                    if (debug) MessageSendHelper.sendWarningMessage("Cancelled lightning sound")
                }

            }
        }
    }

    @Suppress("unused")
    private enum class Cancel {
        ALL, GLOBAL_ONLY
    }
}