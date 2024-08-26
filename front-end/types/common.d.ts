export type Page = {
    currentPage?: number | undefined;
    pageSize?: number | undefined;
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