import type { Page } from "@/types/common";

export type ModelInfo = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    cover?: Blob | string;
    entity?: Blob | string;
    category?: Category;
    visible?: string;
};

export type Category = {
    style: string;
    type: string;
}

export type ModelsShowBarPage = Page & {
    
}