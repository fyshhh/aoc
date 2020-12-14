function parseinput()::Vector{String}
    return readlines("y2020/day14/input.in")
end

function partone(vec::Vector{<:AbstractString})

    dict = Dict{Int, Int}()
    mask = Vector{Char}(undef, 36)
    digs = Vector{Int}(undef, 36)
    bres = Vector{Int}(undef, 36)

    for str in vec
        if startswith(str, "mask")
            mask .= collect(split(str)[3])
        else
            m = match(r"mem\[(?<add>.+)\] = (?<val>.+)", str)
            digits!(digs, parse(Int, m[ :val]), base = 2)
            for i in eachindex(bres)
                bres[i] = mask[i] == 'X' ? digs[37 - i] : parse(Int, mask[i])
            end
            dict[parse(Int, m[ :add])] = parse(Int, join(bres), base = 2)
        end
    end

    dict |> values |> sum |> println

end

function parttwo(vec::Vector{<:AbstractString})

    dict = Dict{Int, Int}()
    mask = Vector{Char}(undef, 36)
    digs = Vector{Int}(undef, 36)
    badd = Set{Vector{Int}}()

    for str in vec
        if startswith(str, "mask")
            mask .= collect(split(str)[3])
        else
            m = match(r"mem\[(?<add>.+)\] = (?<val>.+)", str)
            digits!(digs, parse(Int, m[ :add]), base = 2)
            empty!(badd)
            push!(badd, Vector{Int}(undef, 36))
            for i in eachindex(digs)
                if mask[i] == '0'
                    (v -> v[i] = digs[37 - i]).(badd)
                elseif mask[i] == '1'
                    (v -> v[i] = 1).(badd)
                else
                    scpy = Set{Vector{Int}}()
                    sizehint!(scpy, length(badd) * 2)
                    for v in badd
                        vcpy1 = copy(v)
                        vcpy1[i] = 0
                        push!(scpy, vcpy1)
                        vcpy2 = copy(v)
                        vcpy2[i] = 1
                        push!(scpy, vcpy2)
                    end
                    badd = scpy
                end
            end

            for v in badd
                dict[parse(Int, join(v), base = 2)] = parse(Int, m[ :val])
            end

        end
    end

    dict |> values |> sum |> println

end

vec = parseinput()
partone(vec)
parttwo(vec)
