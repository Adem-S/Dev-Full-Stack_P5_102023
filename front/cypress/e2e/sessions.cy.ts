describe("Sessions spec", () => {
  const sessions = [
    {
      id: 1,
      name: "Session 1",
      description: "Session 1",
      date: new Date("2023-11-15"),
      teacher_id: 1,
      users: [2, 3, 4],
      createdAt: new Date("2023-11-15"),
      updatedAt: new Date("2023-11-15"),
    },
    {
      id: 2,
      name: "Session 2",
      description: "Session 2",
      date: new Date("2023-11-15"),
      teacher_id: 2,
      users: [2, 3, 4],
      createdAt: new Date("2023-11-15"),
      updatedAt: new Date("2023-11-15"),
    },
  ];

  const updatedSession = { ...sessions[0], name: "Session 1.1" };

  const newSession = {
    id: 3,
    name: "Session 3",
    description: "Session 3",
    date: new Date("2023-11-15"),
    teacher_id: 3,
    users: [2, 3, 4],
    createdAt: new Date("2023-11-15"),
    updatedAt: new Date("2023-11-15"),
  };

  const teacher = {
    id: 1,
    firstName: "teacherFirstName",
    lastName: "teacherLastName",
  };

  beforeEach(() => {
    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      sessions
    ).as("session");

    cy.intercept(
      {
        method: "POST",
        url: "/api/session",
      },
      newSession
    ).as("session");

    cy.intercept(
      {
        method: "GET",
        url: "/api/session/1",
      },
      sessions[0]
    ).as("session");

    cy.intercept(
      {
        method: "DELETE",
        url: "/api/session/1",
      },
      {}
    ).as("session");

    cy.intercept(
      {
        method: "PUT",
        url: "/api/session/1",
      },
      updatedSession
    ).as("session");

    cy.intercept(
      {
        method: "GET",
        url: "/api/teacher",
      },
      [teacher]
    ).as("session");

    cy.intercept(
      {
        method: "GET",
        url: "/api/teacher/1",
      },
      teacher
    ).as("session");

    cy.login();
    cy.url().should("include", "/sessions");
  });

  it("Get list sessions", () => {
    cy.get(".mat-card-title").should("contain", sessions[0].name);
    cy.get(".mat-card-title").should("contain", sessions[1].name);
  });

  it("Create a new session", () => {
    cy.wait(1000);

    cy.get(".mat-raised-button").contains("Create").click();

    cy.get("input[formControlName=name]").type(newSession.name);

    cy.get("input[formControlName=date]").type("2023-11-15");

    cy.get("mat-select[formControlName=teacher_id]")
      .click()
      .get("mat-option")
      .contains(teacher.firstName)
      .click();

    cy.get("textarea[formControlName=description]").type(
      newSession.description
    );

    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      [...sessions, newSession]
    ).as("session");

    cy.get("button[type=submit]").click();

    cy.get(".mat-card-title").should("contain", newSession.name);
    cy.url().should("include", "/sessions");
    cy.get(".mat-snack-bar-container").should("contain", "Session created !");
  });

  it("Get details of the first session and delete", () => {
    cy.wait(1000);
    cy.get(".mat-raised-button").contains("Detail").first().click();
    cy.url().should("include", "/detail/1");

    cy.get("h1").should("contain", sessions[0].name);
    cy.get(".mat-card-subtitle").should("contain", teacher.firstName);
    cy.get(".mat-card-content").should("contain", "3 attendees");

    cy.get(".mat-raised-button").contains("Delete").click();

    cy.url().should("include", "/sessions");
    cy.get(".mat-snack-bar-container").should("contain", "Session deleted !");
  });

  it("Edit the first session", () => {
    cy.wait(1000);
    cy.get(".mat-raised-button").contains("Edit").first().click();
    cy.url().should("include", "/update/1");
    cy.get("h1").should("contain", "Update session");

    cy.get("input[formControlName=name]").should(
      "have.value",
      sessions[0].name
    );

    cy.get(".mat-select-value-text").contains(teacher.firstName);

    cy.get("textarea[formControlName=description]").should(
      "have.value",
      sessions[0].description
    );

    cy.intercept(
      {
        method: "GET",
        url: "/api/session",
      },
      [updatedSession, sessions[1]]
    ).as("session");

    cy.get("input[formControlName=name]").clear().type("Session 1.1");

    cy.get("button[type=submit]").click();

    cy.get(".mat-card-title").should("contain", updatedSession.name);
    cy.url().should("include", "/sessions");
    cy.get(".mat-snack-bar-container").should("contain", "Session updated !");
  });
});
