package me.baruna.gatedetector;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import me.baruna.gatedetector.config.Config;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class SignUtil {
    public static SignGate getSign(Vector3i loc) {
        SignGate sign = null;
        try {
            sign = Config.getGates().getNode().getNode(
                    String.valueOf(loc.getX()))
                    .getNode(String.valueOf(loc.getZ()))
                    .getNode(String.valueOf(loc.getY()))
                    .getValue(TypeToken.of(SignGate.class));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        return sign;
    }
}
