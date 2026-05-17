"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
const scanner_1 = require("../utils/scanner");
const router = (0, express_1.Router)();
router.get("/:id", (req, res) => {
    const moviesDir = process.env.MOVIES_DIR || "./movies";
    const thumbnailsDir = process.env.THUMBNAILS_DIR || "./thumbnails";
    const movies = (0, scanner_1.scanMovies)(moviesDir, thumbnailsDir, "");
    const movie = movies.find((m) => m.id === req.params.id);
    if (!movie) {
        return res.status(404).json({
            success: false,
            error: "Movie not found",
        });
    }
    const filePath = path_1.default.join(moviesDir, movie.filename);
    if (!fs_1.default.existsSync(filePath)) {
        return res.status(404).json({
            success: false,
            error: "File not found on disk",
        });
    }
    const stat = fs_1.default.statSync(filePath);
    const fileSize = stat.size;
    const range = req.headers.range;
    const ext = path_1.default.extname(movie.filename).toLowerCase();
    const mimeTypes = {
        ".mp4": "video/mp4",
        ".mkv": "video/x-matroska",
        ".avi": "video/x-msvideo",
        ".mov": "video/quicktime",
        ".webm": "video/webm",
    };
    const contentType = mimeTypes[ext] || "video/mp4";
    if (range) {
        const parts = range.replace(/bytes=/, "").split("-");
        const start = parseInt(parts[0], 10);
        const end = parts[1]
            ? parseInt(parts[1], 10)
            : fileSize - 1;
        const chunkSize = end - start + 1;
        const stream = fs_1.default.createReadStream(filePath, {
            start,
            end,
        });
        res.writeHead(206, {
            "Content-Range": `bytes ${start}-${end}/${fileSize}`,
            "Accept-Ranges": "bytes",
            "Content-Length": chunkSize,
            "Content-Type": contentType,
        });
        stream.pipe(res);
    }
    else {
        res.writeHead(200, {
            "Content-Length": fileSize,
            "Content-Type": contentType,
        });
        fs_1.default.createReadStream(filePath).pipe(res);
    }
});
exports.default = router;
