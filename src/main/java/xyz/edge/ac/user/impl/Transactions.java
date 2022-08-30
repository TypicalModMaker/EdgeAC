package xyz.edge.ac.user.impl;

import java.util.Iterator;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.out.ping.WrappedPacketOutPing;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.play.in.pong.WrappedPacketInPong;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import xyz.edge.ac.packetutils.PacketUtil;
import io.github.retrooper.packetevents.utils.list.ConcurrentList;
import java.util.concurrent.atomic.AtomicInteger;
import xyz.edge.ac.util.type.Pair;
import java.util.concurrent.ConcurrentLinkedQueue;
import xyz.edge.ac.user.User;

public class Transactions
{
    private final User data;
    private final ConcurrentLinkedQueue<Pair<Short, Long>> transactionsSent;
    private final AtomicInteger lastTransactionSent;
    private final int lastTransactionAtStartOfTick = 0;
    private final ConcurrentList<Short> didWeSendThatTrans;
    private final AtomicInteger transactionIDCounter;
    private final ConcurrentLinkedQueue<Pair<Integer, Runnable>> syncTransactionMap;
    private final AtomicInteger packetLastTransactionReceived;
    private long transactionPing;
    private long playerClockAtLeast;
    private int transTicks;
    private int trannieRe;
    private int trannieSent;
    private boolean transactionReceive;
    
    public void handleReceive(final PacketUtil packet) {
        final byte packetID = packet.getPacketId();
        if (packetID == -107) {
            final WrappedPacketInTransaction transaction = new WrappedPacketInTransaction(packet.getRawPacket());
            final short id = transaction.getActionNumber();
            if (id <= 0 && this.addTransactionResponse(id)) {
                packet.setCancelled(true);
            }
        }
        if (packetID == 28) {
            final WrappedPacketInPong pong = new WrappedPacketInPong(packet.getRawPacket());
            final int id2 = pong.getId();
            if (id2 == (short)id2 && this.addTransactionResponse((short)id2)) {
                packet.setCancelled(true);
            }
        }
    }
    
    public int sendTransaction() {
        final short transactionID = this.getNextTransactionID(1);
        try {
            this.addTransactionSend(transactionID);
            if (ServerVersion.getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                PacketEvents.get().getPlayerUtils().sendPacket(this.data.getPlayer(), new WrappedPacketOutPing(transactionID));
            }
            else {
                PacketEvents.get().getPlayerUtils().sendPacket(this.data.getPlayer(), new WrappedPacketOutTransaction(0, transactionID, false));
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
        return transactionID;
    }
    
    public boolean addTransactionResponse(final short id) {
        Pair<Short, Long> data = null;
        boolean hasID = false;
        for (final Pair<Short, Long> iterator : this.transactionsSent) {
            if (iterator.getX() == id) {
                hasID = true;
                break;
            }
        }
        if (hasID) {
            do {
                data = this.transactionsSent.poll();
                if (data == null) {
                    break;
                }
                final int incrementingID = this.packetLastTransactionReceived.incrementAndGet();
                this.transactionPing = System.currentTimeMillis() - data.getY();
                this.transTicks = (int)(this.transactionPing / 50L);
                this.playerClockAtLeast = System.currentTimeMillis() - this.transactionPing;
                this.transactionReceive = true;
                this.tickUpdates(this.syncTransactionMap, incrementingID);
            } while (data.getX() != id);
        }
        return data != null && data.getX() == id;
    }
    
    public void handleSent(final PacketUtil packet) {
        final byte packetID = packet.getPacketId();
        if (packetID == -48) {
            final WrappedPacketInTransaction transaction = new WrappedPacketInTransaction(packet.getRawPacket());
            final short id = transaction.getActionNumber();
            if (id <= 0 && this.didWeSendThatTrans.remove((Object)id)) {
                this.transactionsSent.add(new Pair<Short, Long>(id, System.currentTimeMillis()));
                this.lastTransactionSent.getAndIncrement();
            }
        }
        if (packetID == 29) {
            final WrappedPacketInPong pong = new WrappedPacketInPong(packet.getRawPacket());
            final int id2 = pong.getId();
            if (id2 == (short)id2) {
                final Short shortID = (short)id2;
                if (this.didWeSendThatTrans.remove(shortID)) {
                    this.transactionsSent.add(new Pair<Short, Long>(shortID, System.currentTimeMillis()));
                    this.lastTransactionSent.getAndIncrement();
                }
            }
        }
    }
    
    private void tickUpdates(final ConcurrentLinkedQueue<Pair<Integer, Runnable>> map, final int transaction) {
        for (Pair<Integer, Runnable> next = map.peek(); next != null && transaction >= next.getX(); next = map.peek()) {
            map.poll();
            next.getY().run();
        }
    }
    
    public short getNextTransactionID(final int add) {
        return (short)(-1 * (this.transactionIDCounter.getAndAdd(add) & 0x7FFF));
    }
    
    public void addTransactionSend(final short id) {
        this.didWeSendThatTrans.add(id);
    }
    
    public void onTick() {
    }
    
    public void addTransactionHandler(final int id, final Runnable runnable) {
        this.syncTransactionMap.add(new Pair<Integer, Runnable>(id, runnable));
    }
    
    public User getData() {
        return this.data;
    }
    
    public ConcurrentLinkedQueue<Pair<Short, Long>> getTransactionsSent() {
        return this.transactionsSent;
    }
    
    public AtomicInteger getLastTransactionSent() {
        return this.lastTransactionSent;
    }
    
    public int getLastTransactionAtStartOfTick() {
        this.getClass();
        return 0;
    }
    
    public ConcurrentList<Short> getDidWeSendThatTrans() {
        return this.didWeSendThatTrans;
    }
    
    public AtomicInteger getTransactionIDCounter() {
        return this.transactionIDCounter;
    }
    
    public ConcurrentLinkedQueue<Pair<Integer, Runnable>> getSyncTransactionMap() {
        return this.syncTransactionMap;
    }
    
    public AtomicInteger getPacketLastTransactionReceived() {
        return this.packetLastTransactionReceived;
    }
    
    public long getTransactionPing() {
        return this.transactionPing;
    }
    
    public long getPlayerClockAtLeast() {
        return this.playerClockAtLeast;
    }
    
    public int getTransTicks() {
        return this.transTicks;
    }
    
    public int getTrannieRe() {
        return this.trannieRe;
    }
    
    public int getTrannieSent() {
        return this.trannieSent;
    }
    
    public boolean isTransactionReceive() {
        return this.transactionReceive;
    }
    
    public Transactions(final User data) {
        this.transactionsSent = new ConcurrentLinkedQueue<Pair<Short, Long>>();
        this.lastTransactionSent = new AtomicInteger(0);
        this.didWeSendThatTrans = new ConcurrentList<Short>();
        this.transactionIDCounter = new AtomicInteger(0);
        this.syncTransactionMap = new ConcurrentLinkedQueue<Pair<Integer, Runnable>>();
        this.packetLastTransactionReceived = new AtomicInteger(0);
        this.data = data;
    }
}
