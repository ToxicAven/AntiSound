import com.lambda.client.plugin.api.Plugin

internal object AntiSoundLoader: Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here
        modules.add(AntiSound)
    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}