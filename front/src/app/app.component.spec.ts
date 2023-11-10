import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatToolbarModule } from "@angular/material/toolbar";
import { RouterTestingModule } from "@angular/router/testing";
import { expect } from "@jest/globals";

import { AppComponent } from "./app.component";
import { SessionService } from "./services/session.service";
import { of } from "rxjs";
import { Router } from "@angular/router";

describe("AppComponent", () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  const mockSessionService = {
    $isLogged: jest.fn(() => of(true)),
    logOut: jest.fn(() => of({})),
  };

  const mockSessionServiceNotConnected = {
    $isLogged: jest.fn(() => of(false)),
    logOut: jest.fn(() => of({})),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule, MatToolbarModule],
      providers: [],
      declarations: [AppComponent],
    }).compileComponents();
  });

  describe("When im logged in", () => {
    beforeEach(() => {
      TestBed.overrideProvider(SessionService, {
        useValue: mockSessionService,
      });

      fixture = TestBed.createComponent(AppComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should display routes when the user is logged in", () => {
      const appElement = fixture.nativeElement;
      expect(appElement.textContent).not.toContain("Login");
      expect(appElement.textContent).not.toContain("Register");
      expect(appElement.textContent).toContain("Sessions");
      expect(appElement.textContent).toContain("Account");
      expect(appElement.textContent).toContain("Logout");
    });

    it("should log out", () => {
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, "navigate");

      component.logout();

      expect(mockSessionService.logOut).toHaveBeenCalled();

      expect(navigateSpy).toHaveBeenCalledWith([""]);
    });
  });

  describe("When im not connected", () => {
    beforeEach(() => {
      TestBed.overrideProvider(SessionService, {
        useValue: mockSessionServiceNotConnected,
      });

      fixture = TestBed.createComponent(AppComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should display routes when the user is not connected", () => {
      const appElement = fixture.nativeElement;
      expect(appElement.textContent).toContain("Login");
      expect(appElement.textContent).toContain("Register");
      expect(appElement.textContent).not.toContain("Sessions");
      expect(appElement.textContent).not.toContain("Account");
      expect(appElement.textContent).not.toContain("Logout");
    });
  });
});
