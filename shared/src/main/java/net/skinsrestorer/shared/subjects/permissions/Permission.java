/*
 * SkinsRestorer
 *
 * Copyright (C) 2023 SkinsRestorer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package net.skinsrestorer.shared.subjects.permissions;

import ch.jalu.configme.SettingsManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.skinsrestorer.shared.config.CommandConfig;

import java.util.Collection;
import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PROTECTED)
public class Permission {
    @Getter
    private final String permissionString;

    public boolean checkPermission(SettingsManager settings, Predicate<String> predicate) {
        Collection<PermissionGroup> permissionGroups = PermissionGroup.getGrantedBy(this);
        if (permissionGroups.isEmpty()) {
            return internalCheckPermission(predicate);
        }

        if (permissionGroups.contains(PermissionGroup.getDefaultGroup())
                && settings.getProperty(CommandConfig.FORCE_DEFAULT_PERMISSIONS)) {
            return true;
        }

        for (PermissionGroup permissionGroup : permissionGroups) {
            if (permissionGroup.getBasePermission().internalCheckPermission(predicate)
                    || permissionGroup.getWildcard().internalCheckPermission(predicate)) {
                return true;
            }
        }

        return internalCheckPermission(predicate);
    }

    public boolean internalCheckPermission(Predicate<String> predicate) {
        return predicate.test(permissionString);
    }
}
