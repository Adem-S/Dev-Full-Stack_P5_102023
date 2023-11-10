import { HttpClientModule } from "@angular/common/http";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterTestingModule } from "@angular/router/testing";
import { expect } from "@jest/globals";
import { AuthService } from "../../services/auth.service";
import { SessionService } from "src/app/services/session.service";

import { LoginComponent } from "./login.component";
import { ListComponent } from "../../../sessions/components/list/list.component";
import { Router } from "@angular/router";

describe("LoginComponent", () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [AuthService, SessionService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: "sessions", component: ListComponent },
        ]),
        HttpClientTestingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it("should create and initialize 'hide' to true and 'onError' to false", () => {
    expect(component).toBeTruthy();
    expect(component.hide).toBe(true);
    expect(component.onError).toBe(false);
  });

  it("should validate the form with valid data", () => {
    const form = component.form;

    expect(form.contains("email")).toBeTruthy();
    expect(form.contains("password")).toBeTruthy();

    let email = form.get("email");
    let password = form.get("password");

    email?.setValue("test@test.com");
    password?.setValue("password");

    expect(email?.valid).toBeTruthy();
    expect(password?.valid).toBeTruthy();
    expect(form.valid).toBeTruthy();
  });

  it("should not validate the form with invalid data", () => {
    const form = component.form;

    expect(form.contains("email")).toBeTruthy();
    expect(form.contains("password")).toBeTruthy();

    let email = form.get("email");
    let password = form.get("password");

    email?.setValue("test1");
    password?.setValue("");

    expect(email?.valid).toBeFalsy();
    expect(password?.valid).toBeFalsy();
    expect(form.valid).toBeFalsy();
  });

  it("should login successfully", () => {
    const form = component.form;
    const authService = TestBed.inject(AuthService);
    const sessionService = TestBed.inject(SessionService);
    const router = TestBed.inject(Router);

    const authServiceSpy = jest.spyOn(authService, "login");
    const sessionServiceSpy = jest.spyOn(sessionService, "logIn");
    const navigateSpy = jest.spyOn(router, "navigate");

    form.get("email")?.setValue("test@example.com");
    form.get("password")?.setValue("password123");

    const sessionInfo = {
      token: "token",
      type: "type",
      id: 1,
      username: "testuser",
      firstName: "John",
      lastName: "Doe",
      admin: true,
    };

    component.submit();

    const req = httpMock.expectOne("api/auth/login");
    expect(req.request.method).toBe("POST");
    req.flush(sessionInfo);

    fixture.detectChanges();

    expect(authServiceSpy).toHaveBeenCalledWith({
      email: "test@example.com",
      password: "password123",
    });

    expect(sessionService.isLogged).toBe(true);

    expect(sessionServiceSpy).toHaveBeenCalledWith(sessionInfo);
    expect(navigateSpy).toHaveBeenCalledWith(["/sessions"]);
  });

  it("should not login with error", () => {
    expect(component.onError).toBe(false);

    const form = component.form;
    const authService = TestBed.inject(AuthService);
    const sessionService = TestBed.inject(SessionService);
    const router = TestBed.inject(Router);

    const authServiceSpy = jest.spyOn(authService, "login");
    const sessionServiceSpy = jest.spyOn(sessionService, "logIn");
    const navigateSpy = jest.spyOn(router, "navigate");

    form.get("email")?.setValue("test@example.com");
    form.get("password")?.setValue("password123");

    component.submit();

    const req = httpMock.expectOne("api/auth/login");
    expect(req.request.method).toBe("POST");
    req.flush("error", { status: 401, statusText: "Not Found" });

    fixture.detectChanges();

    expect(authServiceSpy).toHaveBeenCalledWith({
      email: "test@example.com",
      password: "password123",
    });

    expect(component.onError).toBe(true);

    expect(sessionService.isLogged).toBe(false);
    expect(sessionServiceSpy).not.toHaveBeenCalled();
    expect(navigateSpy).not.toHaveBeenCalledWith(["/sessions"]);
  });
});
