package com.sapuseven.untis.helpers.issues

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.sapuseven.untis.R


class GithubIssue(type: Type, log: String) : Issue(type, log) {

	private var c: Context? = null

	override fun launch(context: Context) {
		c = context
		val uri = Uri.Builder()
				.scheme("https")
				.authority("github.com")
				.path("/SapuSeven/BetterUntis/issues/new")
				.appendQueryParameter("title", generateTitle())
				.appendQueryParameter("body", generateBody())
				.appendQueryParameter("labels", "bug") //TODO: Make this label thing work

		val browserIntent = Intent(Intent.ACTION_VIEW, uri.build())
		context.startActivity(browserIntent)
	}

	private fun generateTitle() = when (type) {
		Type.CRASH -> "[Crash Report]"
		Type.EXCEPTION -> "[Bug Report]"
		Type.OTHER -> ""
	}

	private val version: String get() {
		val pInfo = c?.packageManager?.getPackageInfo(c?.packageName ?: return "", 0)
		return c?.getString(R.string.preference_info_app_version_desc,
				pInfo?.versionName,
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
					pInfo?.longVersionCode
				} else {
					@Suppress("DEPRECATION")
					pInfo?.versionCode?.toLong()
				}
		) ?: ""
	}

	private val installationSource: String get() {
		return c?.packageManager?.getInstallerPackageName(c?.packageName ?:"") ?: ""
	}

	private fun generateBody() =
		"<details>\n" +
		"<summary>Logs</summary>\n" +
		"\n" +
		"```\n" +
		"$log\n" +
		"```\n" +
		"</details>\n" +
		"\n" +
		"**Additional information**\n" +
		"\n" +
		"- Android version: _${Build.VERSION.RELEASE}_\n" +
		"- BetterUntis version: _${version}_\n" +
		"- Installation source: _${installationSource}_"
}
