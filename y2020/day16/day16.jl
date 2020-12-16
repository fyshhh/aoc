function parseinput()::Vector{<:AbstractString}
    return split(read("y2020/day16/input.in", String), "\n\n")
end

function partone(vec::Vector{<:AbstractString})

    valid = Vector{Int}(undef, 0)

    for line in split(vec[1], "\n")
        m = match(r".+: (?<min1>.+)-(?<max1>.+) or (?<min2>.+)-(?<max2>.+)", line)
        union!(valid, parse(Int, m[ :min1]):parse(Int, m[ :max1]))
        union!(valid, parse(Int, m[ :min2]):parse(Int, m[ :max2]))
    end

    flush(stdout)
    (split(vec[3], r"(\n|,)") .|> s -> tryparse(Int, s)) |> v -> filter(i -> !(i in valid || isnothing(i)), v) |> sum |> println

end

function parttwo(vec::Vector{<:AbstractString})

    order = Vector{String}(undef, 20)
    passes = Vector{Vector{Int}}(undef, 0)
    orders = Dict{String, Set{Int}}()
    fields = Dict{String, Pair{UnitRange{Int}, UnitRange{Int}}}()

    for line in split(vec[1], "\n")
        m = match(r"(?<field>.+): (?<min1>.+)-(?<max1>.+) or (?<min2>.+)-(?<max2>.+)", line)
        fields[m[ :field]] = parse(Int, m[ :min1]):parse(Int, m[ :max1]) => parse(Int, m[ :min2]):parse(Int, m[ :max2])
    end

    valid = union((values(fields) .|> p -> union(p...))...)

    (match(r"your ticket:\n(?<nums>.+)", vec[2])[ :nums] |> s -> split(s, ",") .|> s -> parse(Int, s)) |> collect |> v -> push!(passes, v)

    sum = 0

    for line in split(vec[3], "\n")
        if occursin(r"([0-9]|,)+", line)
            temp = (split(line, ",") .|> s -> parse(Int, s)) |> collect
            if issubset(temp, valid)
                push!(passes, temp)
            end
        end
    end

    passes = hcat(passes...)

    for field in keys(fields)
        r = union(fields[field].first, fields[field].second)
        for i in 1:20
            c = @view passes[i, :]
            if issubset(c, r)
                orders[field] = push!(get(orders, field, Set{Int}()), i)
            end
        end
    end

    queue = sort(collect(orders), by = p -> length(p.second))
    assigned = Set{Int}()

    while !isempty(queue)
        pair = popfirst!(queue)
        v = setdiff(pair.second, assigned) |> collect
        @assert length(v) == 1
        order[v[1]] = pair.first
        push!(assigned, v[1])
    end

    flush(stdout)
    (1:20 |> r -> filter(i -> startswith(order[i], "departure"), r) .|> i -> passes[i, 1]) |> r -> reduce(*, r) |> println

end

inp = parseinput()
partone(inp)
parttwo(inp)
