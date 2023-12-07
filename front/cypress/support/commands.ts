declare namespace Cypress {
  interface Chainable<Subject> {
    login(): Chainable<Subject>;
  }
}

Cypress.Commands.add("login", () => {
  cy.visit("/login");
  cy.intercept("POST", "/api/auth/login", {
    body: {
      id: 1,
      username: "userName",
      firstName: "firstName",
      lastName: "lastName",
      admin: true,
    },
  });

  cy.get("input[formControlName=email]").type("yoga@studio.com");
  cy.get("input[formControlName=password]").type(
    `${"test!1234"}{enter}{enter}`
  );
});
