describe("Logout spec", () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");
    cy.login();
  });

  it("Logout successfully", () => {
    cy.get(".link").contains("Logout").should("exist").click();
    cy.url().should("include", "");

    cy.get(".link").contains("Login").should("exist");
    cy.get(".link").contains("Register").should("exist");
  });
});
