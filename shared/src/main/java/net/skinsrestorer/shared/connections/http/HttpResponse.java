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
package net.skinsrestorer.shared.connections.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.shared.exception.DataRequestExceptionShared;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class HttpResponse {
    private static final Gson GSON = new Gson();
    private final int statusCode;
    private final String body;
    private final Map<String, List<String>> headers;

    public <T> T getBodyAs(Class<T> clazz) throws DataRequestException {
        try {
            return GSON.fromJson(body, clazz);
        } catch (JsonSyntaxException e) {
            throw new DataRequestExceptionShared(e);
        }
    }
}
