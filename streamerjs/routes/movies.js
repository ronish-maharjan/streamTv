"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.setBaseUrl = setBaseUrl;
const express_1 = require("express");
const scanner_1 = require("../utils/scanner");
const router = (0, express_1.Router)();
let BASE_URL = "";
function setBaseUrl(url) {
    BASE_URL = url;
}
function getMovies() {
    const moviesDir = process.env.MOVIES_DIR || "./movies";
    const thumbnailsDir = process.env.THUMBNAILS_DIR || "./thumbnails";
    return (0, scanner_1.scanMovies)(moviesDir, thumbnailsDir, BASE_URL);
}
router.get("/", (req, res) => {
    const movies = getMovies();
    res.json({
        success: true,
        data: movies,
    });
});
router.get("/:id", (req, res) => {
    const movies = getMovies();
    const movie = movies.find((m) => m.id === req.params.id);
    if (!movie) {
        return res.status(404).json({
            success: false,
            error: "Movie not found",
        });
    }
    res.json({ success: true, data: movie });
});
exports.default = router;
