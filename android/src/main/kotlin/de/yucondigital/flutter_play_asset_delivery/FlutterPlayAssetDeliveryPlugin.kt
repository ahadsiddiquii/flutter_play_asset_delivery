package de.yucondigital.flutter_play_asset_delivery

import android.content.res.AssetManager
import androidx.annotation.NonNull
import com.google.android.play.core.assetpacks.AssetPackManagerFactory
import com.google.android.play.core.assetpacks.AssetPackManager

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.io.FileOutputStream

/** FlutterPlayAssetDeliveryPlugin */
class FlutterPlayAssetDeliveryPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var assetManager: AssetManager
    private lateinit var assetList: List<String>
    private lateinit var assetPackManager: AssetPackManager
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        assetManager = flutterPluginBinding.applicationContext.resources.assets
        fetchAllAssets()
        assetPackManager =
            AssetPackManagerFactory.getInstance(flutterPluginBinding.applicationContext)
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_play_asset_delivery")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

        if (call.method == "getAssetFile") {
            var assetName: String = call.arguments.toString()

            if (assetList.contains(assetName)) {
                val file = File.createTempFile("asset_", null)
                copyAssetToFile(assetName, file)
                result.success(file.absolutePath)
            } else {
                result.error("Asset not found", "Asset could not be found.", null)
            }
        } else {
            result.notImplemented()
        }
    }


    private fun copyAssetToFile(assetName: String, file: File) {
        assetManager.open(assetName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4096)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
            }
        }
    }
    private fun fetchAllAssets() {
        assetList = assetManager.list("")?.asList() ?: emptyList()
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
