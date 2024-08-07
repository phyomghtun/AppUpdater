package com.appupdater.htun

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView

object UpdateDialogUtil {
    fun showUpdateDialog(context: Context, appPackageName: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        // Create the custom view programmatically
        val relativeLayout = RelativeLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }

        val updateTitle = TextView(context).apply {
            id = View.generateViewId()
            text = "New Update Available!"
            textSize = 20f
            setTextColor(Color.BLACK)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.CENTER_HORIZONTAL)
                setMargins(0, 0, 0, 16)
            }
        }

        val updateMessage = TextView(context).apply {
            id = View.generateViewId()
            text = "A new version of the app is available. Please update to the latest version to enjoy new features and improvements."
            textSize = 16f
            setTextColor(Color.BLACK)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.BELOW, updateTitle.id)
                setMargins(0, 0, 0, 24)
            }
        }

        val updateButton = Button(context).apply {
            id = View.generateViewId()
            text = "UPDATE"
            setBackgroundColor(Color.parseColor("#000000")) // Set background color to black
            setTextColor(Color.WHITE)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.BELOW, updateMessage.id)
                addRule(RelativeLayout.CENTER_HORIZONTAL)
            }
        }

        relativeLayout.addView(updateTitle)
        relativeLayout.addView(updateMessage)
        relativeLayout.addView(updateButton)

        // Set the custom view as the content view of the dialog
        dialog.setContentView(relativeLayout)

        // Set up click listener for the button
        updateButton.setOnClickListener {
            // Perform click animation
            updateButton.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(150)
                .withEndAction {
                    updateButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            openPlayStore(context, appPackageName)
                        }
                }
        }

        dialog.show()
    }

    private fun openPlayStore(context: Context, appPackageName: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            intent.setPackage(appPackageName)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
            context.startActivity(webIntent)
        }
    }
}