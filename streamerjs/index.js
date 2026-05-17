"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const os_1 = __importDefault(require("os"));
const express_1 = __importDefault(require("express"));
const cors_1 = __importDefault(require("cors"));
const dotenv_1 = __importDefault(require("dotenv"));
const path_1 = __importDefault(require("path"));
const movies_1 = __importStar(require("./routes/movies"));
const stream_1 = __importDefault(require("./routes/stream"));
const auth_1 = require("./middleware/auth");
dotenv_1.default.config();
const app = (0, express_1.default)();
const PORT = Number(process.env.PORT) || 3000;
const THUMBNAILS_DIR = process.env.THUMBNAILS_DIR || "./thumbnails";
app.use((0, cors_1.default)());
app.use(express_1.default.json());
app.use("/thumbnails", express_1.default.static(path_1.default.resolve(THUMBNAILS_DIR)));
// Get IP
function getLocalIP() {
    const nets = os_1.default.networkInterfaces();
    for (const name of Object.keys(nets)) {
        for (const net of nets[name] || []) {
            const familyV4Value = typeof net.family === "string" ? "IPv4" : 4;
            if (net.family === familyV4Value && !net.internal) {
                return net.address;
            }
        }
    }
    return "localhost";
}
function getBaseUrl() {
    return `http://${getLocalIP()}:${PORT}`;
}
// ✅ IMPORTANT: set base URL globally (no router injection)
(0, movies_1.setBaseUrl)(getBaseUrl());
app.use("/movies", auth_1.authMiddleware, movies_1.default);
app.use("/stream", auth_1.authMiddleware, stream_1.default);
app.get("/health", (_, res) => {
    res.json({
        success: true,
        message: "Streaming server running 🎬",
    });
});
app.listen(PORT, "0.0.0.0", () => {
    const ip = getLocalIP();
    console.log(`🎬 Running:`);
    console.log(`http://localhost:${PORT}`);
    console.log(`http://${ip}:${PORT}`);
});
