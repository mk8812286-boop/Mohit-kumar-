const { initializeTestEnvironment, assertFails, assertSucceeds } = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;

before(async () => {
  testEnv = await initializeTestEnvironment({
    projectId: "gen-lang-client-0508685698",
    firestore: {
      rules: fs.readFileSync("firestore.rules", "utf8"),
      host: "127.0.0.1",
      port: 8085
    }
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Public can read shops and products", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  await assertSucceeds(unauthedDb.collection("shops").get());
  await assertSucceeds(unauthedDb.collection("products").get());
});

test("Unauthenticated cannot read users", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthedDb.collection("users").get());
});

test("Authenticated user can write own profile", async () => {
  const aliceDb = testEnv.authenticatedContext("alice").firestore();
  await assertSucceeds(aliceDb.collection("users").doc("alice").set({
    id: "alice",
    displayName: "Alice",
    role: "CUSTOMER"
  }));
});

test("User cannot write another user profile", async () => {
  const aliceDb = testEnv.authenticatedContext("alice").firestore();
  await assertFails(aliceDb.collection("users").doc("bob").set({
    id: "bob",
    displayName: "Bob",
    role: "CUSTOMER"
  }));
});
