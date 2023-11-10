import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { expect } from "@jest/globals";
import { SessionService } from "src/app/services/session.service";
import { SessionApiService } from "../../services/session-api.service";
import { lastValueFrom, of } from "rxjs";

import { ListComponent } from "./list.component";
import { Router } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { FormComponent } from "../form/form.component";
import { DetailComponent } from "../detail/detail.component";

describe("ListComponent", () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSessionServiceNotAdmin = {
    sessionInformation: {
      admin: false,
    },
  };

  const sessionsTest = [
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

  const mockSessionApiService = {
    all: () => of(sessionsTest),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: "create", component: FormComponent },
          { path: "detail/:id", component: DetailComponent },
          { path: "update/:id", component: FormComponent },
        ]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
      ],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    }).compileComponents();
  });

  describe("When user is admin", () => {
    beforeEach(() => {
      TestBed.overrideProvider(SessionService, {
        useValue: mockSessionService,
      });

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should return the list of sessions", async () => {
      await expect(lastValueFrom(component.sessions$)).resolves.toEqual(
        sessionsTest
      );
    });

    it("should render a list of sessions", () => {
      const sessionElements = fixture.nativeElement.querySelectorAll(
        ".item .mat-card-title"
      );
      expect(sessionElements.length).toBe(2);
      expect(sessionElements[0].textContent).toContain("Session 1");
      expect(sessionElements[1].textContent).toContain("Session 2");
    });

    it("should render the 'create' button", () => {
      const router = TestBed.inject(Router);
      const createButton = fixture.nativeElement.querySelector(
        'button[routerLink="create"]'
      );
      expect(createButton).toBeTruthy();
      createButton.click();
      fixture.detectChanges();
      expect(router.url).toBe(`/create`);
    });

    it("should render the 'detail' button of first session", () => {
      const router = TestBed.inject(Router);

      const detailButton = fixture.nativeElement.querySelectorAll("button")[1];

      expect(detailButton).toBeTruthy();
      detailButton.click();
      fixture.detectChanges();
      expect(router.url).toBe(`/detail/1`);
    });

    it("should render the 'edit' button of first session", () => {
      const router = TestBed.inject(Router);

      const editButton = fixture.nativeElement.querySelectorAll("button")[2];

      expect(editButton).toBeTruthy();
      editButton.click();
      fixture.detectChanges();
      expect(router.url).toBe(`/update/1`);
    });
  });

  describe("When user is not admin", () => {
    beforeEach(() => {
      TestBed.overrideProvider(SessionService, {
        useValue: mockSessionServiceNotAdmin,
      });

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it("should not render the 'create' button", () => {
      const createButton = fixture.nativeElement.querySelector(
        'button[routerLink="create"]'
      );
      expect(createButton).toBeFalsy();
    });

    it("should not render the 'edit' button", () => {
      const createButton = fixture.nativeElement.querySelector(
        'button[routerLink="update/1"]'
      );
      expect(createButton).toBeFalsy();
    });
  });
});
