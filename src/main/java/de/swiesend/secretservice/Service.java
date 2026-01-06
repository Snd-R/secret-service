package de.swiesend.secretservice;

import de.swiesend.secretservice.handlers.Messaging;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.Variant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Service extends Messaging implements de.swiesend.secretservice.interfaces.Service {

    public static final List<Class<? extends DBusSignal>> signals = Arrays.asList(
            CollectionCreated.class, CollectionChanged.class, CollectionDeleted.class);
    private Session session = null;

    public Service(DBusConnection connection) {
        super(connection, signals,
                Static.Service.SECRETS,
                Static.ObjectPaths.SECRETS,
                Static.Interfaces.SERVICE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<byte[], DBusPath> openSession(String algorithm, Variant input) {
        Object[] params = send("OpenSession", "sv", algorithm, input);
        if (params == null) return null;
        session = new Session((DBusPath) params[1], this);

        // dbus-java starting from version 5.1 always returns List of Byte objects instead of array of primitive bytes
        final var byteList = ((Variant<List<Byte>>) params[0]).getValue();
        final var byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return new Pair<>(byteArray, (DBusPath) params[1]);
    }

    @Override
    public Pair<DBusPath, DBusPath> createCollection(Map<String, Variant> properties, String alias) {
        String a;
        if (alias == null) {
            a = "";
        } else {
            a = alias;
        }
        Object[] params = send("CreateCollection", "a{sv}s", properties, a);
        if (params == null) return null;
        return new Pair(params[0], params[1]);
    }

    @Override
    public Pair<DBusPath, DBusPath> createCollection(Map<String, Variant> properties) {
        return createCollection(properties, "");
    }

    @Override
    public Pair<List<DBusPath>, List<DBusPath>> searchItems(Map<String, String> attributes) {
        Object[] params = send("SearchItems", "a{ss}", attributes);
        if (params == null) return null;
        return new Pair(params[0], params[1]);
    }

    @Override
    public Pair<List<DBusPath>, DBusPath> unlock(List<DBusPath> objects) {
        Object[] params = send("Unlock", "ao", objects);
        if (params == null) return null;
        return new Pair(params[0], params[1]);
    }

    @Override
    public Pair<List<DBusPath>, DBusPath> lock(List<DBusPath> objects) {
        Object[] params = send("Lock", "ao", objects);
        if (params == null) return null;
        return new Pair(params[0], params[1]);
    }

    @Override
    public void lockService() {
        send("LockService", "");
    }

    @Override
    public DBusPath changeLock(DBusPath collection) {
        Object[] params = send("ChangeLock", "o", collection);
        if (params == null) return null;
        return (DBusPath) params[0];
    }

    @Override
    public Map<DBusPath, Secret> getSecrets(List<DBusPath> items, DBusPath session) {
        Object[] params = send("GetSecrets", "aoo", items, session);
        if (params == null) return null;
        return (Map<DBusPath, Secret>) params[0];
    }

    @Override
    public DBusPath readAlias(String name) {
        Object[] params = send("ReadAlias", "s", name);
        if (params == null) return null;
        return (DBusPath) params[0];
    }

    @Override
    public void setAlias(String name, DBusPath collection) {
        send("SetAlias", "so", name, collection);
    }

    @Override
    public List<DBusPath> getCollections() {
        Variant response = getProperty("Collections");
        if (response == null) return null;
        return (ArrayList<DBusPath>) response.getValue();
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public String getObjectPath() {
        return Static.ObjectPaths.SECRETS;
    }

    public Session getSession() {
        return session;
    }

}
