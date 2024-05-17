/*
 * This file is part of ViaFabricPlus - https://github.com/FlorianMichael/ViaFabricPlus
 * Copyright (C) 2021-2024 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and RK_01/RaphiMC
 * Copyright (C) 2023-2024 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.florianmichael.viafabricplus.protocoltranslator.translator;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.protocols.v1_13_2to1_14.packet.ClientboundPackets1_14;
import com.viaversion.nbt.tag.Tag;
import de.florianmichael.viafabricplus.ViaFabricPlus;
import de.florianmichael.viafabricplus.protocoltranslator.ProtocolTranslator;

public class TextComponentTranslator {

    private static final UserConnection DUMMY_USER_CONNECTION = ProtocolTranslator.createDummyUserConnection(ProtocolTranslator.NATIVE_VERSION, ProtocolVersion.v1_14);

    /**
     * Converts a ViaVersion 1.14 text component to a native text component, both in JSON format
     *
     * @param component The ViaVersion 1.14 text component
     * @return The ViaVersion text component for the native version
     */
    public static Tag via1_14toViaLatest(final JsonElement component) {
        try {
            final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_14.OPEN_SCREEN, DUMMY_USER_CONNECTION);
            wrapper.write(Types.VAR_INT, 1); // window id
            wrapper.write(Types.VAR_INT, 0); // type id
            wrapper.write(Types.COMPONENT, component); // title

            wrapper.resetReader();
            wrapper.user().getProtocolInfo().getPipeline().transform(Direction.CLIENTBOUND, State.PLAY, wrapper);

            wrapper.read(Types.VAR_INT); // window id
            wrapper.read(Types.VAR_INT); // type id
            return wrapper.read(Types.TAG); // title
        } catch (Throwable t) {
            ViaFabricPlus.global().getLogger().error("Error converting ViaVersion 1.14 text component to native text component", t);
            return null;
        }
    }

}
