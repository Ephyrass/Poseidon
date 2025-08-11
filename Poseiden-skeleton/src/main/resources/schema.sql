-- Création des tables pour l'application Poseidon

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(125) NOT NULL UNIQUE,
    password VARCHAR(125) NOT NULL,
    fullname VARCHAR(125) NOT NULL,
    role VARCHAR(125) NOT NULL
);

CREATE TABLE IF NOT EXISTS BidList (
    BidListId SERIAL PRIMARY KEY,
    account VARCHAR(30) NOT NULL,
    type VARCHAR(30) NOT NULL,
    bidQuantity DOUBLE PRECISION,
    askQuantity DOUBLE PRECISION,
    bid DOUBLE PRECISION,
    ask DOUBLE PRECISION,
    benchmark VARCHAR(125),
    bidListDate TIMESTAMP,
    commentary VARCHAR(125),
    security VARCHAR(125),
    status VARCHAR(10),
    trader VARCHAR(125),
    book VARCHAR(125),
    creationName VARCHAR(125),
    creationDate TIMESTAMP,
    revisionName VARCHAR(125),
    revisionDate TIMESTAMP,
    dealName VARCHAR(125),
    dealDate TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trade (
    TradeId SERIAL PRIMARY KEY,
    account VARCHAR(30) NOT NULL,
    type VARCHAR(30) NOT NULL,
    buyQuantity DOUBLE PRECISION,
    sellQuantity DOUBLE PRECISION,
    buyPrice DOUBLE PRECISION,
    sellPrice DOUBLE PRECISION,
    tradeDate TIMESTAMP,
    security VARCHAR(125),
    status VARCHAR(10),
    trader VARCHAR(125),
    benchmark VARCHAR(125),
    book VARCHAR(125),
    creationName VARCHAR(125),
    creationDate TIMESTAMP,
    revisionName VARCHAR(125),
    revisionDate TIMESTAMP,
    dealName VARCHAR(125),
    dealDate TIMESTAMP
);

CREATE TABLE IF NOT EXISTS CurvePoint (
    Id SERIAL PRIMARY KEY,
    CurveId INTEGER,
    asOfDate TIMESTAMP,
    term DOUBLE PRECISION,
    "value" DOUBLE PRECISION,
    creationDate TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Rating (
    Id SERIAL PRIMARY KEY,
    moodysRating VARCHAR(125),
    sandPRating VARCHAR(125),
    fitchRating VARCHAR(125),
    orderNumber INTEGER
);

CREATE TABLE IF NOT EXISTS RuleName (
    Id SERIAL PRIMARY KEY,
    name VARCHAR(125),
    description VARCHAR(125),
    json VARCHAR(125),
    template VARCHAR(512),
    sqlStr VARCHAR(125),
    sqlPart VARCHAR(125)
);
