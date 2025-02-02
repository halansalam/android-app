/*
 * Copyright (c) 2019 Proton Technologies AG
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
package com.protonvpn.android.models.config

enum class VpnProtocol {
    OpenVPN,
    IKEv2,
    WireGuard,
    Smart;

    fun localAgentEnabled(): Boolean = this == WireGuard

    // TODO remove this parameter after wireguard exits beta
    fun displayName(wireguardWithBeta: Boolean = true): String {
        return if (this == WireGuard) {
            if (wireguardWithBeta) {
                "WireGuard (beta)"
            } else {
                "WireGuard"
            }
        } else {
            toString()
        }
    }
}
