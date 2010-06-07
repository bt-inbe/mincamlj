open KNormal

// �ִ��Τ���δؿ� (caml2html: beta_find)
def find(x, env) = try {
	M.find(x, env)
} catch {
	case Not_found => x
}

// �´���롼�������� (caml2html: beta_g)
def g(env,e) e match {
	case Unit => Unit
	case Int(i) => Int(i)
	case Float(d) => Float(d)
	case Neg(x) => Neg(find(x, env))
	case Add(x, y) => Add(find(x, env), find(y, env))
	case Sub(x, y) => Sub(find(x, env), find(y, env))
	case FNeg(x) => FNeg(find(x, env))
	case FAdd(x, y) => FAdd(find(x, env), find(y, env))
	case FSub(x, y) => FSub(find(x, env), find(y, env))
	case FMul(x, y) => FMul(find(x, env), find(y, env))
	case FDiv(x, y) => FDiv(find(x, env), find(y, env))
	case IfEq(x, y, e1, e2) => IfEq(find(x, env), find(y, env), g(env, e1), g(env, e2))
	case IfLE(x, y, e1, e2) => IfLE(find(x, env), find(y, env), g(env, e1), g(env, e2))
	case Let((x, t), e1, e2) => (* let�Φ´��� (caml2html: beta_let) *)
		g(env, e1) match {
		case Var(y) =>
			println("beta-reducing "+x+" = "+y+"@.");
			g(M.add(x, y, env), e2)
		case e1dash =>
			val e2dash = g(env, e2);
			Let((x, t), e1dash, e2dash)
		}
	case LetRec(Fundef(xt,yts,e1), e2) =>
		LetRec(Fundef(xt,yts,g(env, e1)), g(env, e2))
	case Var(x) => Var(find(x, env)) // �ѿ����ִ� (caml2html: beta_var)
	case Tuple(xs) => Tuple( xs.map(x => find(x, env)) )
	case LetTuple(xts, y, e) => LetTuple(xts, find(y, env), g(env, e))
	case Get(x, y) => Get(find(x, env), find(y, env))
	case Put(x, y, z) => Put(find(x, env), find(y, env), find(z, env))
	case App(g, xs) => App(find(g, env), xs.map(x => find(x, env)) )
	case ExtArray(x) => ExtArray(x)
	case ExtFunApp(x, ys) => ExtFunApp(x, ys.map(y => find(y, env)) )
}

def f() = g(M.empty)
