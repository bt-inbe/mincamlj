open KNormal

// ����饤��Ÿ������ؿ��κ��祵���� (caml2html: inline_threshold)
var threshold = 0 // Main��-inline���ץ����ˤ�ꥻ�åȤ����

def size(e) = e match {
	case IfEq(_, _, e1, e2)         => 1 + size(e1) + size (e2)
	case IfLE(_, _, e1, e2)         => 1 + size(e1) + size (e2)
	case Let(_, e1, e2)             => 1 + size(e1) + size (e2)
	case LetRec(Fundef(_,_,e1), e2) => 1 + size(e1) + size (e2)
	case LetTuple(_, _, e)          => 1 + size(e)
	case _                          => 1
}

// ����饤��Ÿ���롼�������� (caml2html: inline_g)
def g(env,e) = e match {
	case IfEq(x, y, e1, e2) => IfEq(x, y, g(env, e1), g(env, e2))
	case IfLE(x, y, e1, e2) => IfLE(x, y, g(env, e1), g(env, e2))
	case Let(xt, e1, e2)    => Let(xt, g(env, e1), g(env, e2))

	// �ؿ�����ξ�� (caml2html: inline_letrec)
	case LetRec(Fundef((x, t), yts, e1), e2) => 
		val env = if (size(e1) > threshold) env else M.add(x,(yts, e1), env);
		LetRec(Fundef((x, t), yts, g(env, e1)), g(env, e2))
	// �ؿ�Ŭ�Ѥξ�� (caml2html: inline_app)
	case App(x, ys) if(M.mem(x, env) => 
		val (zs, e) = M.find(x, env);
		println( "inlining "+x+"@.");
		val envdash = List.fold_left2 (
				(envdash, (z, t), y) => M.add(z, y, envdash),
				M.empty,
				zs,
				ys
			);
			Alpha.g(envdash, e)
	case LetTuple(xts, y, e) => LetTuple(xts, y, g(env, e))
	case e => e
}

def f(e) = g(M.empty, e)
