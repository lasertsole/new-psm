export type Page<T> = {
    current?: number;
    size?: number;
    total?: number;
    records?: T[];
    pages?: number;
};

export type TagBarItem = {
    tabName:string,
    index:number,
    paddingLeft?:String,
    paddingRight?:String,
    path: String | RouteLocationAsRelativeGeneric | RouteLocationAsPathGeneric | undefined
};

export type OptionInfo = {
    label:string,
    value: number
}

export type FilterItem = {
    selectList?: OptionInfo[][],
    switchList?: OptionInfo[]
}