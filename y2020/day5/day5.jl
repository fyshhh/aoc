function parseseat(line::AbstractString)::Tuple{Int, Int}
    h = (0, 127)
    v = (0, 7)
    while (length(line) > 0)
        inst = first(line, 1)
        if inst == "F"
            h = (h[1], (h[1] + h[2] - 1) / 2)
        elseif inst == "B"
            h = ((h[1] + h[2] + 1) / 2, h[2])
        elseif inst == "L"
            v = (v[1], (v[1] + v[2] - 1) / 2)
        elseif inst == "R"
            v = ((v[1] + v[2] + 1) / 2, v[2])
        end
        line = chop(line, head = 1, tail = 0)
    end
    return (h[1], v[1])
end

function main()
    input = split(read("y2020/day5/input.in", String), "\n")
    seats = Array{Tuple{Int, Int}}(undef, length(input))
    map!(parseseat, seats, input)
    flush(stdout)   # something's wrong with juno's repl
    println(maximum(t -> 8 * t[1] + t[2], seats))
    total = vec(collect(Iterators.product(0:127, 0:7)))
    setdiff!(total, seats)
    filter!(t -> !(t[1] == 0 || t[1] == 127), total)
    filter!(t -> in((t[1] + 1, t[2]), seats) && in((t[1] - 1, t[2]), seats),
        total)
    println((t -> 8 * t[1] + t[2])(total[1]))
end

main()
