describe("Not found spec", () => {
  it("Show page not found", () => {
    cy.visit("/example");
    cy.get("h1").should("contain", "Page not found !");
  });
});
