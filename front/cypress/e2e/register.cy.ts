describe("Register spec", () => {
  it("Not Register with error", () => {
    cy.visit("/register");

    cy.intercept("POST", "/api/auth/register", {
      statusCode: 500,
      body: "Internal Server Error",
    });

    cy.get("input[formControlName=firstName]").type("John");
    cy.get("input[formControlName=lastName]").type("Doe");
    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type("test!1234");

    cy.get("button[type=submit]").click();

    cy.get(".error").should("contain", "An error occurred");

    cy.url().should("include", "/register");
  });
  it("Register successfully", () => {
    cy.visit("/register");

    cy.intercept("POST", "/api/auth/register", {
      body: {
        message: "User registered successfully!",
      },
    });

    cy.get("input[formControlName=firstName]").type("John");
    cy.get("input[formControlName=lastName]").type("Doe");
    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type("test!1234");

    cy.get("button[type=submit]").click();

    cy.url().should("include", "/login");
  });
});
