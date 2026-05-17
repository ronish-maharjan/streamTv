"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.authMiddleware = authMiddleware;
function authMiddleware(req, res, next) {
    const apiKey = req.headers["x-api-key"] || req.query.apiKey;
    if (!process.env.API_KEY) {
        next(); // No API key configured = open access
        return;
    }
    if (apiKey !== process.env.API_KEY) {
        res.status(401).json({ success: false, error: "Unauthorized" });
        return;
    }
    next();
}
