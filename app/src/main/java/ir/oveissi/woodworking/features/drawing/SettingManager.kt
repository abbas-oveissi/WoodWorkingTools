package ir.oveissi.woodworking.features.drawing

import kotlin.properties.Delegates

object SettingManager {
    var rotateAndZoomEnabled: Boolean by Delegates.observable(true) { _, _, _ ->
        settingObservers.forEach { it() }
    }
    var forceMaxBrightness:  Boolean by Delegates.observable(false) { _, _, _ ->
        settingObservers.forEach { it() }
    }

    var edgeDetectionEnabled: Boolean by Delegates.observable(false) { _, _, _ ->
        settingObservers.forEach { it() }
    }

    var flipVerticallyImage: Boolean by Delegates.observable(false) { _, _, _ ->
        settingObservers.forEach { it() }
    }
    var flipHorizontalImage: Boolean by Delegates.observable(false) { _, _, _ ->
        settingObservers.forEach { it() }
    }

    val settingObservers = mutableListOf<() -> Unit>()

}