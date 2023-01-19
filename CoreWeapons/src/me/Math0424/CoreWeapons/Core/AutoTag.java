package me.Math0424.CoreWeapons.Core;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AutoTag<T extends SerializableItem<T>> implements PersistentDataType<byte[], T> {

    private final Class<T> type;

    public AutoTag(Class<T> type) {
        this.type = type;
    }

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<T> getComplexType() {
        return type;
    }

    @Override
    public byte[] toPrimitive(T a, PersistentDataAdapterContext itemTagAdapterContext) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            Map<String, Object> map = new HashMap<>();
            a.serialize(map);
            oos.writeObject(map);

            oos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public T fromPrimitive(byte[] bytes, PersistentDataAdapterContext itemTagAdapterContext) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            T t = type.newInstance();
            t.deSerialize((Map<String, Object>) ois.readObject());

            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
