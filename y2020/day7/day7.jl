struct Bag
    name::String
    prnt::Vector{String}
    chld::Vector{Pair{String, Int}}
    function Bag(name::AbstractString)
        return new(String(name),
            Vector{String}(undef, 0),
            Vector{Pair{String, Int}}(undef, 0))
    end
end

function generate()::Dict{String, Bag}
    dict = Dict{String, Bag}()
    readlines("y2020/day7/input.in") .|>
        s -> match(r"(?<parent>.+) bags contain (?<children>.+).", s) |>
            r1 -> begin
                p = get!(dict, r1[ :parent], Bag(r1[ :parent]))
                r1[ :children] |> s -> split(s, ", ") .|>
                    s -> chop(s, head = 0, tail = 4) |> strip |>
                    s -> match(r"(?<amt>[0-9]+) (?<type>.+)", s) |>
                        r2 -> begin
                            if r2 != nothing
                                amt = parse(Int, r2[ :amt])
                                c = get!(dict, r2[ :type], Bag(r2[ :type]))
                                push!(p.chld, c.name => amt)
                                push!(c.prnt, p.name)
                            end
                        end
            end
    return dict
end

function partone(dict::Dict{String, Bag})
    prnts = Set{String}()
    queue = ["shiny gold"]
    while !isempty(queue)
        bag = getindex(dict, pop!(queue))
        bag.prnt .|> s -> begin
            push!(prnts, s)
            push!(queue, s)
        end
    end
    flush(stdout)
    prnts |> length |> println
end

function parttwo(dict::Dict{String, Bag})
    count = 0
    queue = ["shiny gold" => 1]
    while !isempty(queue)
        pair = pop!(queue)
        bag = getindex(dict, pair.first)
        bag.chld .|> s -> begin
            count += s.second * pair.second
            push!(queue, s.first => s.second * pair.second)
        end
    end
    count |> println
end

function main()
    dict = generate()
    partone(dict)
    parttwo(dict)
end

main()
