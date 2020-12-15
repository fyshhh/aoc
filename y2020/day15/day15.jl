function parseinput()::Vector{Int}
    return read("y2020/day15/input.in", String) |> s -> split(s, ",") .|> s -> parse(Int, s)
end

function recite(vec::Vector{Int}, val::Int)

    dict = Dict{Int, Vector{Int}}()
    nums = Vector{Int}(undef, val)

    for i in eachindex(nums)
        if i <= length(vec)
            nums[i] = vec[i]
        elseif dict[nums[i - 1]] |> length == 1
            nums[i] = 0
        else
            nums[i] = last(dict[nums[i - 1]]) - dict[nums[i - 1]][length(dict[nums[i - 1]]) - 1]
        end

        if haskey(dict, nums[i])
            push!(dict[nums[i]], i)
        else
            dict[nums[i]] = [i]
        end

    end

    flush(stdout)
    nums[val] |> println

end

inp = parseinput()
recite(inp, 2020)
recite(inp, 30000000)           # i'm not proud of how long this takes so i'm going to try to reduce it
