package com.psm.utils.Model;

public class KVModel<K, V> {
    private K key;
    private V value;

    public KVModel() {
    }

    public KVModel(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public KVModel(KVModel<K, V> kvModel) {
        this.key = kvModel.key;
        this.value = kvModel.value;
    }


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KVModel{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
