import type { ContactsDBItem, MessageDBItem } from '~/types/chat';
import Dexie, { type EntityTable } from 'dexie';

const db = new Dexie('psmDB') as Dexie & {
  ContactsDBItems: EntityTable< ContactsDBItem, 'id'>;// 联系人记录的id不使用自动生成
  MessageDBItems: EntityTable< MessageDBItem, 'id'>;// 聊天记录的id使用自动生成
};

db.version(1).stores({
  ContactsDBItems: '++id, srcUserId, &[srcUserId+tgtUserId], name, timestamp',
  MessageDBItems: '++id, [maxUserId+minUserId], timestamp'
});

export { db };