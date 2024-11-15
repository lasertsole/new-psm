export type StorageItem = {
    storeName: string;
    primaryKey: { keyPath: string, autoIncrement?: boolean };
    indexes?: { name: string, keyPath: string | Iterable<string>, options?: IDBIndexParameters }[];
};