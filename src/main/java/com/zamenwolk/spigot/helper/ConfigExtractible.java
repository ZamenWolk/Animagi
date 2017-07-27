/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.helper;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by Martin on 20/07/2017.
 */
public interface ConfigExtractible
{
    void getFromConfig(Object config);
}
