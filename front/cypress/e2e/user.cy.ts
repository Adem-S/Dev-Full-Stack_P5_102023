describe("User spec", () => {
  const user = {
    firstName: "John",
    lastName: "Doe",
    email: "test@test.com",
    admin: true,
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  beforeEach(() => {
    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");

    cy.intercept(
      {
        method: "GET",
        url: "/api/user/1",
      },
      user
    ).as("session");

    cy.login();
  });

  it("Get user information", () => {
    cy.get(".link").contains("Account").should("exist").click();
    cy.url().should("include", "/me");
    cy.contains(user.firstName + " " + user.lastName.toUpperCase());
    cy.contains("Email: " + user.email);
    cy.contains("You are admin");

    // click go back button
    cy.get(".mat-icon-button").click();
    cy.url().should("include", "/sessions");
  });
});
