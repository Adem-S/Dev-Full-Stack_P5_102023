describe("Login spec", () => {
  beforeEach(() => {
    cy.visit("/login");
  });

  it("Not login with error", () => {
    cy.intercept("POST", "/api/auth/login", {
      statusCode: 500,
      body: "Internal Server Error",
    });

    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");

    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type(
      `${"test!1234"}{enter}{enter}`
    );

    cy.get(".error").should("contain", "An error occurred");
    cy.url().should("include", "/login");
  });

  it("Login successfully and navigate to /sessions", () => {
    cy.intercept("POST", "/api/auth/login", {
      body: {
        id: 1,
        username: "userName",
        firstName: "firstName",
        lastName: "lastName",
        admin: true,
      },
    });

    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      []
    ).as("session");

    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type(
      `${"test!1234"}{enter}{enter}`
    );
    cy.url().should("include", "/sessions");
  });
});
