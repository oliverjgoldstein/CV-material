diff(x, x, 1).
diff(plus(U,V),x,plus(RU,RV)):-diff(U,x,RU),diff(V,x,RV).
diff(sub(U,V),x,sub(RU,RV)):- sub(diff(U,x,RU),diff(V,x,RV)).
diff(neg(U),x,RU) :- diff(U, x, neg(RU)).
diff(times(U, V), x, plus( times(U, RV), times(V, RU))) :- diff(V, x, RV), diff(U,x,RU).

% dx/dx = 1
% d(-U)/dx = -(dU/dx)
% d(U+V)/dx = dU/dx + dV/dx
% d(U-V)/dx = dU/dx - dV/dx
% d(U*V)/dx = U*(dV/dx) + V*(dU/dx)

% U*1 = U
% U+0 = U
% U+U = 2*U
% U-U = 0


simp(times(U, 1), U).
simp(times(1, U), U).
simp(plus(U, U), times(2,U)).
simp(plus(U, 0), U).
simp(plus(0, U), U).
simp(sub(U,U), 0).

simp(times(U, J), Result) :- simp(U, Us), simp(J, Js), Result=times(Us, Js).
simp(plus(U, J), Result) :- simp(U, Us), simp(J, Js), Result=plus(Us, Js).
simp(sub(U, J), Result) :- simp(U, Us), simp(J, Js), Result=sub(Us, Js).

simp(X, X).
