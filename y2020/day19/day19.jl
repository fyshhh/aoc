function parseinput()::Vector{Vector{<:AbstractString}}
    return read("y2020/day19/input.in", String) |> s -> split(s, "\n\n") .|> s -> split(s, "\n")
end

function partone(inp::Vector{Vector{<:AbstractString}})

    @assert length(inp) == 2

    dict = Dict{Int, String}()

    inp[1] .|> s -> match(r"(?<index>.+): (?<cond>.+)", s) |> m -> dict[parse(Int, m[:index])] = m[:cond]

    function creatergx(ind::Int)
        str = dict[ind]
        if (contains(str, r"(a|b)"))
            return str[2:2]
        else
            return (split(str, " | ") .|> s -> begin
                (split(s) .|> i -> creatergx(parse(Int, i))) |> v -> *(v...)
            end) |> v -> reduce((a, b) -> *(a, "|", b), v) |> v -> *("(", v, ")")
        end
    end

    rgx = Regex(creatergx(0))

    flush(stdout)
    inp[2] |> v -> filter(s -> findfirst(rgx, s) == 1:length(s), v) |> length |> println

end

function parttwo(inp::Vector{Vector{<:AbstractString}})

    @assert length(inp) == 2

    dict = Dict{Int, String}()

    inp[1] .|> s -> match(r"(?<index>.+): (?<cond>.+)", s) |> m -> dict[parse(Int, m[:index])] = m[:cond]

    function creatergx(ind::Int)
        str = dict[ind]
        if (contains(str, r"(a|b)"))
            return str[2:2]
        else
            return (split(str, " | ") .|> s -> begin
                (split(s) .|> i -> creatergx(parse(Int, i))) |> v -> *(v...)
            end) |> v -> reduce((a, b) -> *(a, "|", b), v) |> v -> *("(", v, ")")
        end
    end

    # matching strings are of the format (42){x + y}(31){y}, where x and y >= 1; it suffices to reverse
    # search the number of occurrences of (31) and ensure the number of occurrences of (42) is greater

    function customreverse(str::AbstractString)
        return (str |> reverse |> collect .|> c -> c == '(' ? ')' : c == ')' ? '(' : c) |> join
    end

    rgx42 = creatergx(42) |> customreverse
    rgx31 = creatergx(31) |> customreverse

    function custommatch(str::AbstractString)
        val = 0
        tmp = reverse(str)
        mat = findfirst(Regex(rgx31), tmp)
        while !isnothing(mat) && first(mat) == 1
            val += 1
            tmp = SubString(tmp, length(mat) + 1)
            mat = findfirst(Regex(rgx31), tmp)
        end
        return val > 0 && findfirst(Regex(*(rgx42, "{$(val)}", rgx42, "+")), tmp) == 1:length(tmp)
    end

    flush(stdout)
    inp[2] |> v -> filter(custommatch, v) |> length |> println

end

inp = parseinput()
partone(inp)
parttwo(inp)
