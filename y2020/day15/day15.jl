function parseinput()::Vector{Int}
    return read("y2020/day15/input.in", String) |> s -> split(s, ",") .|> s -> parse(Int, s)
end

function recite(vec::Vector{Int}, val::Int)

    dict = Dict{Int, Pair{Int, Int}}()
    curr = 0
    prev = 0

    for i in 1:val
        if i <= length(vec)
            curr = vec[i]
        elseif dict[prev].first == 0
            curr = 0
        else
            curr = dict[prev] |> p -> p.second - p.first
        end

        if haskey(dict, curr)
            dict[curr] = dict[curr].second => i
        else
            dict[curr] = 0 => i
        end

        prev = curr
    end

    flush(stdout)
    curr |> println

end

inp = parseinput()
recite(inp, 2020)
recite(inp, 30000000)           # around 3 seconds but idk how to make it faster
