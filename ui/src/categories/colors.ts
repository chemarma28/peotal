export const PRESET_COLORS = [
    "#7cb342",
    "#5c6bc0",
    "#ef6c00",
    "#ab47bc",
    "#29b6f6",
    "#26a69a",
    "#8d6e63",
    "#607d8b",
    "#c2185b",
    "#f9a825",
];

export function isPresetColor(color: string) {
    return PRESET_COLORS.includes(color.toLowerCase());
}
