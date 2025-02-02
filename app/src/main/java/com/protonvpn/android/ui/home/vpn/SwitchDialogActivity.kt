/*
 * Copyright (c) 2021 Proton Technologies AG
 *
 * This file is part of ProtonVPN.
 *
 * ProtonVPN is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonVPN is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonVPN.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.protonvpn.android.ui.home.vpn

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import com.protonvpn.android.R
import com.protonvpn.android.components.BaseActivityV2
import com.protonvpn.android.components.ContentLayout
import com.protonvpn.android.components.NotificationHelper
import com.protonvpn.android.databinding.ActivitySwitchDialogBinding
import com.protonvpn.android.utils.CountryTools
import com.protonvpn.android.utils.ServerManager
import javax.inject.Inject

@ContentLayout(R.layout.activity_switch_dialog)
class SwitchDialogActivity : BaseActivityV2<ActivitySwitchDialogBinding, ViewModel>() {

    @Inject lateinit var serverManager: ServerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() = with(binding) {
        val reconnectionNotification =
            intent.getParcelableExtra<NotificationHelper.ReconnectionNotification>(EXTRA_NOTIFICATION_DETAILS)!!
        reconnectionNotification.reconnectionInformation?.let {
            initReconnectionUI(it)
        }
        textDescription.text = reconnectionNotification.content
        textTitle.text = reconnectionNotification.title
        reconnectionNotification.fullScreenDialog?.let { fullScreenDialog ->
            buttonBack.setOnClickListener {
                fullScreenDialog.cancelToastMessage?.let {
                    Toast.makeText(baseContext, it, Toast.LENGTH_LONG).show()
                }
                finish()
            }
            fullScreenDialog.fullScreenIcon?.let { it1 -> image.setImageResource(it1) }
        }

        layoutUpsell.root.isVisible = reconnectionNotification.fullScreenDialog?.hasUpsellLayout == true
        layoutUpsell.textManyCountries.text = getString(R.string.upsell_many_countries, serverManager.getVpnCountries().size)
        layoutUpsell.textDeviceCount.text = getString(R.string.upsell_device_count, getString(R.string.device_count))
        reconnectionNotification.action?.let { actionItem ->
            buttonUpgrade.text = actionItem.title
            buttonUpgrade.setOnClickListener { actionItem.pendingIntent.send() }
        } ?: run {
            buttonBack.isVisible = false
            buttonUpgrade.text = getString(R.string.got_it)
            buttonUpgrade.setOnClickListener {
                finish()
            }
        }
    }

    private fun initReconnectionUI(reconnectionInformation: NotificationHelper.ReconnectionInformation) =
        with(binding.itemSwitchLayout) {
            root.isVisible = true

            textFromServer.text = reconnectionInformation.fromServerName
            textToServer.text = reconnectionInformation.toServerName
            reconnectionInformation.toCountrySecureCore?.let {
                imageToCountrySc.setImageResource(
                    CountryTools.getFlagResource(this@SwitchDialogActivity, it)
                )
                imageToCountrySc.isVisible = true
                arrowToSc.isVisible = true
            }
            reconnectionInformation.fromCountrySecureCore?.let {
                imageFromCountrySc.setImageResource(
                    CountryTools.getFlagResource(
                        this@SwitchDialogActivity, it
                    )
                )
                imageFromCountrySc.isVisible = true
                arrowFromSc.isVisible = true
            }
            imageToCountry.setImageResource(
                CountryTools.getFlagResource(this@SwitchDialogActivity, reconnectionInformation.toCountry)
            )
            imageFromCountry.setImageResource(
                CountryTools.getFlagResource(this@SwitchDialogActivity, reconnectionInformation.fromCountry)
            )
        }

    override fun initViewModel() {

    }

    companion object {
        const val EXTRA_NOTIFICATION_DETAILS = "ReconnectionNotification"
    }
}
