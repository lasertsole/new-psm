import { defineStore } from 'pinia';

interface State {
    name:string,
    age:number
}
export const useLocalStore = defineStore('localStore', {/*localStore(key),不同内容可以新建不同文件设置不同key*/
    state: (): State => ({
        name: '',
        age:0,
    }),
    actions: {
        setName(name: string) {
            this.name = name
        },
        setAge(age: number) {
            this.age = age
        },
        getName() {
            return this.name;
        },
        getAge(age: number) {
            return this.age;
        },
    },
    // 注意：persist定义要做判断，因为localStorage/sessionStorage是客户端参数，所以需要加process.client
    persist: process.client && {
        storage: localStorage,
    },
})