function parseinput()::Pair{Int, Vector{String}}
    return readlines("y2020/day13/input.in") |> a -> parse(Int, a[1]) => (split(a[2], ","))
end

function partone(pair::Pair{Int, Vector{String}})
    (filter(!=("x"), pair.second) .|> s -> parse(Int, s)) |> v -> mapreduce(i -> i => i - (pair.first % i), (p, q) -> p.second < q.second ? p : q, v) |> p -> println(p.first * p.second)
end

function parttwo(pair::Pair{Int, Vector{String}})
    dict = Dict{Int, Int}()              # dict describing congruences
    for (i, s) in enumerate(pair.second)
        if s != "x"
            dict[-(i - 1)] = parse(Int, s)
        end
    end
    sum = 0
    prod = reduce(*, values(dict))
    for i in keys(dict)
        sum += i * div(prod, dict[i]) * gcdx(div(prod, dict[i]), dict[i])[2]
    end
    mod1(sum, prod) |> println
end

pair = parseinput()
partone(pair)
parttwo(pair)
