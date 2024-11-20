export function max<T extends number | string>(a: T, b: T): T {
    if (a > b) {
        return a;
    } else {
        return b;
    }
}

export function min<T extends number | string>(a: T, b: T): T {
    if (a < b) {
        return a;
    } else {
        return b;
    }
}