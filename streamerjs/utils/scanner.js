"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.scanMovies = scanMovies;
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
const crypto_1 = __importDefault(require("crypto"));
const VIDEO_EXTENSIONS = [".mp4", ".mkv", ".avi", ".mov", ".webm"];
const IMAGE_EXTENSIONS = [".jpg", ".jpeg", ".png", ".webp"];
function generateId(filename) {
    return crypto_1.default
        .createHash("md5")
        .update(filename)
        .digest("hex")
        .slice(0, 12);
}
function formatTitle(filename) {
    return filename
        .replace(/[-_.]/g, " ")
        .replace(/\b\w/g, (c) => c.toUpperCase())
        .trim();
}
function getThumbnail(thumbnailsDir, baseName, baseUrl) {
    for (const ext of IMAGE_EXTENSIONS) {
        const filePath = path_1.default.join(thumbnailsDir, baseName + ext);
        if (fs_1.default.existsSync(filePath)) {
            return `${baseUrl}/thumbnails/${baseName}${ext}`;
        }
    }
    return `${baseUrl}/thumbnails/default.jpg`;
}
function scanMovies(moviesDir, thumbnailsDir, baseUrl) {
    if (!fs_1.default.existsSync(moviesDir)) {
        fs_1.default.mkdirSync(moviesDir, { recursive: true });
    }
    if (!fs_1.default.existsSync(thumbnailsDir)) {
        fs_1.default.mkdirSync(thumbnailsDir, { recursive: true });
    }
    const files = fs_1.default.readdirSync(moviesDir);
    const movies = [];
    for (const file of files) {
        const ext = path_1.default.extname(file).toLowerCase();
        if (!VIDEO_EXTENSIONS.includes(ext))
            continue;
        const baseName = path_1.default.basename(file, ext);
        const filePath = path_1.default.join(moviesDir, file);
        const stats = fs_1.default.statSync(filePath);
        const id = generateId(file);
        movies.push({
            id,
            title: formatTitle(baseName),
            filename: file,
            thumbnail: getThumbnail(thumbnailsDir, baseName, baseUrl),
            description: `Watch ${formatTitle(baseName)} in HD quality.`,
            duration: null,
            size: stats.size,
            streamUrl: `${baseUrl}/stream/${id}`,
            createdAt: stats.birthtime.toISOString(),
        });
    }
    return movies.sort((a, b) => new Date(b.createdAt).getTime() -
        new Date(a.createdAt).getTime());
}
