package alexrnov.cosmichunter

class Class23 {
  fun f() {
    //val s = " fd efv    erf,ef          refds     wqefsd sdfsd dsf     dfdf    "
    val s = "\tgfhgfn\tb\td\t \tdf dfdf\t\tdfg \tfd1\t\t\tewew\t\t"

    val a: List<String> = s.trim().split("\\s+".toRegex())
    println("a.leng th =" + a.size)
    println("a = $a")
    println("two = ${a[1]}")
  }
}